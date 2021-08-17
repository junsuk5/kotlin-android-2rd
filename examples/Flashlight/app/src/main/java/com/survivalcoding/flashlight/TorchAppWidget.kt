package com.survivalcoding.flashlight

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

/**
 * Implementation of App Widget functionality.
 */
class TorchAppWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val widgetText = context.getString(R.string.appwidget_text)
    // RemoteView 객체를 구성합니다.
    val views = RemoteViews(context.packageName, R.layout.torch_app_widget)
    views.setTextViewText(R.id.appwidget_text, widgetText)

    // 실행할 Intent를 작성
    val intent = Intent(context, TorchService::class.java)   // ③
    val pendingIntent = PendingIntent.getService(
        context,
        0,
        intent,
        PendingIntent.FLAG_IMMUTABLE
    )  // ②

    // 위젯을 클릭하면 위에서 정의한 Intent를 실행
    views.setOnClickPendingIntent(R.id.appwidget_layout, pendingIntent) // ①

    // 위젯 관리자에게 위젯을 업데이트하도록 지시합니다.
    appWidgetManager.updateAppWidget(appWidgetId, views)
}