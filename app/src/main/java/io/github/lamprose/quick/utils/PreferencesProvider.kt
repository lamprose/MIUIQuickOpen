package io.github.lamprose.quick.utils

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.text.TextUtils
import io.github.lamprose.quick.utils.PreferencesUtils.getEditor


class PreferencesProvider : ContentProvider() {
    companion object {
        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private var authorities: String = "io.github.lamprose.quick.utils.PreferencesProvider"
        var COLUMNNAME = "SPCOLUMNNAME"

        private const val mStringPath = "string/*/*/"
        const val STRING_CONTENT_URI_CODE = 100

        private const val mIntegerPath = "integer/*/*/"
        const val INTEGER_CONTENT_URI_CODE = 101

        private const val mLongPath = "long/*/*/"
        const val LONG_CONTENT_URI_CODE = 102

        private const val mFloatPath = "float/*/*/"
        const val FLOAT_CONTENT_URI_CODE = 104

        private const val mBooleanPath = "boolean/*/*/"
        const val BOOLEAN_CONTENT_URI_CODE = 105

        private const val mDeletePath = "delete/*/*/"
        const val DELETE_CONTENT_URI_CODE = 106

        private const val mPutsPath = "puts"
        const val PUTS_CONTENT_URI_CODE = 107
    }

    init {
        uriMatcher.addURI(authorities, mStringPath, STRING_CONTENT_URI_CODE)
        uriMatcher.addURI(authorities, "$mStringPath*/", STRING_CONTENT_URI_CODE)
        uriMatcher.addURI(authorities, mIntegerPath, INTEGER_CONTENT_URI_CODE)
        uriMatcher.addURI(authorities, "$mIntegerPath*/", INTEGER_CONTENT_URI_CODE)
        uriMatcher.addURI(authorities, mLongPath, LONG_CONTENT_URI_CODE)
        uriMatcher.addURI(authorities, "$mLongPath*/", LONG_CONTENT_URI_CODE)
        uriMatcher.addURI(authorities, mFloatPath, FLOAT_CONTENT_URI_CODE)
        uriMatcher.addURI(authorities, "$mFloatPath*/", FLOAT_CONTENT_URI_CODE)
        uriMatcher.addURI(authorities, mBooleanPath, BOOLEAN_CONTENT_URI_CODE)
        uriMatcher.addURI(authorities, "$mBooleanPath*/", BOOLEAN_CONTENT_URI_CODE)
        uriMatcher.addURI(authorities, mDeletePath, DELETE_CONTENT_URI_CODE)
        uriMatcher.addURI(authorities, mPutsPath, PUTS_CONTENT_URI_CODE)

    }


    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val model = getModel(uri) ?: return null
        val code: Int = uriMatcher.match(uri)
        if (code == STRING_CONTENT_URI_CODE || code == INTEGER_CONTENT_URI_CODE || code == LONG_CONTENT_URI_CODE || code == FLOAT_CONTENT_URI_CODE || code == BOOLEAN_CONTENT_URI_CODE || code == PUTS_CONTENT_URI_CODE
        ) {
            values?.let { insert(requireContext(), it, model) }
        }
        return uri
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val model: Model? = getModel(uri)
        val code: Int = uriMatcher.match(uri)
        return buildCursor((context)!!, (model)!!, code)
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        val model = getModel(uri) ?: return -1
        val code: Int = uriMatcher.match(uri)
        if (code == STRING_CONTENT_URI_CODE || code == INTEGER_CONTENT_URI_CODE || code == LONG_CONTENT_URI_CODE || code == FLOAT_CONTENT_URI_CODE || code == BOOLEAN_CONTENT_URI_CODE
        ) {
            insert(requireContext(), values!!, model)
        }
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val model = getModel(uri) ?: return -1
        val code: Int = uriMatcher.match(uri)
        if (code == STRING_CONTENT_URI_CODE || code == INTEGER_CONTENT_URI_CODE || code == LONG_CONTENT_URI_CODE || code == FLOAT_CONTENT_URI_CODE || code == BOOLEAN_CONTENT_URI_CODE
        ) {
            delete(requireContext(), model)
        }
        return 0
    }

    override fun getType(uri: Uri): String? {
        return ""
    }


    private fun getModel(uri: Uri): Model? {
        try {
            val model = Model()
            model.spName = uri.pathSegments[1]
            if (uri.pathSegments.size > 2) {
                model.key = uri.pathSegments[2]
            }
            if (uri.pathSegments.size > 3) {
                model.defValue = uri.pathSegments[3]
            }
            return model
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun buildCursor(
        context: Context,
        model: Model,
        code: Int
    ): Cursor? {
        var value: Any? = null
        var defValue: Any? = model.defValue
        when (code) {
            STRING_CONTENT_URI_CODE -> value = if (defValue == null) {
                PreferencesUtils.getString(context, model.spName, model.key)
            } else {
                PreferencesUtils.getString(
                    context,
                    model.spName,
                    model.key,
                    defValue.toString()
                )
            }
            INTEGER_CONTENT_URI_CODE -> if (defValue == null) {
                value = PreferencesUtils.getInt(context, model.spName, model.key)
            } else {
                if (!TextUtils.isDigitsOnly(defValue.toString() + "")) {
                    defValue = -1
                }
                value = PreferencesUtils.getInt(
                    context,
                    model.spName,
                    model.key,
                    (defValue.toString() + "").toInt()
                )
            }
            LONG_CONTENT_URI_CODE -> if (defValue == null) {
                value = PreferencesUtils.getLong(context, model.spName, model.key)
            } else {
                if (!TextUtils.isDigitsOnly(defValue.toString() + "")) {
                    defValue = -1
                }
                value = PreferencesUtils.getLong(
                    context,
                    model.spName,
                    model.key,
                    (defValue.toString() + "").toLong()
                )
            }
            FLOAT_CONTENT_URI_CODE -> value = if (defValue == null) {
                PreferencesUtils.getFloat(context, model.spName, model.key)
            } else {
                PreferencesUtils.getFloat(
                    context,
                    model.spName,
                    model.key,
                    (defValue.toString() + "").toFloat()
                )
            }
            BOOLEAN_CONTENT_URI_CODE -> value = if (defValue == null) {
                PreferencesUtils.getBoolean(context, model.spName, model.key)
                    .toString() + ""
            } else {
                PreferencesUtils.getBoolean(
                    context,
                    model.spName,
                    model.key,
                    java.lang.Boolean.valueOf(defValue.toString() + "")
                ).toString() + ""
            }
            else -> {
            }
        }
        if (value == null) return null
        val columnNames = arrayOf(COLUMNNAME)
        val cursor = MatrixCursor(columnNames)
        val values = arrayOf(value)
        cursor.addRow(values)
        return cursor
    }

    private fun insert(
        context: Context,
        values: ContentValues,
        model: Model
    ) {
        val editor = getEditor(context, model.spName)
        val keys = values.keySet()
        for (key in keys) {
            when (val value = values[key]) {
                is Int -> editor.putInt(key, (value.toString() + "").toInt())
                is Long -> editor.putLong(key, (value.toString() + "").toLong())
                is Float -> editor.putFloat(key, (value.toString() + "").toFloat())
                is Boolean -> editor.putBoolean(
                    key,
                    java.lang.Boolean.valueOf(value.toString() + "")
                )
                else -> editor.putString(key, (value ?: "").toString() + "")
            }
        }
        editor.apply()
    }

    private fun delete(context: Context, model: Model) {
        val editor = getEditor(context, model.spName)
        editor.remove(model.key)
        editor.apply()
    }


    data class Model(
        var spName: String? = null,
        var key: String? = null,
        var defValue: Any? = null
    )

}