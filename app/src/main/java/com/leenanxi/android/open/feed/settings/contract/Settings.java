package com.leenanxi.android.open.feed.settings.contract;

import com.leenanxi.android.open.feed.R;
import com.leenanxi.android.open.feed.settings.contract.SettingsEntries.BooleanSettingsEntry;
import com.leenanxi.android.open.feed.settings.contract.SettingsEntries.EnumSettingsEntry;
import com.leenanxi.android.open.feed.settings.contract.SettingsEntries.StringSettingsEntry;

public class Settings {
    public static final StringSettingsEntry ACTIVE_ACCOUNT_NAME = new StringSettingsEntry(
            R.string.pref_key_active_account_name, R.string.pref_default_value_active_account_name);
    public static final BooleanSettingsEntry AUTO_REFRESH_HOME = new BooleanSettingsEntry(
            R.string.pref_key_auto_refresh_home, R.bool.pref_default_value_auto_refresh_home);
    public static final BooleanSettingsEntry SHOW_TITLE_FOR_LINK_ENTITY = new BooleanSettingsEntry(
            R.string.pref_key_show_title_for_link_entity,
            R.bool.pref_default_value_show_title_for_link_entity);
    public static final EnumSettingsEntry<OpenUrlWithMethod> OPEN_URL_WITH_METHOD =
            new EnumSettingsEntry<>(R.string.pref_key_open_url_with,
                    R.string.pref_default_value_open_url_with, OpenUrlWithMethod.class);
    public static final BooleanSettingsEntry ALWAYS_COPY_TO_CLIPBOARD_AS_TEXT =
            new BooleanSettingsEntry(R.string.pref_key_always_copy_to_clipboard_as_text,
                    R.bool.pref_default_value_always_copy_to_clipboard_as_text);

    public enum OpenUrlWithMethod {
        WEBVIEW,
        INTENT,
        CUSTOM_TABS
    }
}
