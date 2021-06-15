package com.example.notifications

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat

class SettingsActivity : AppCompatActivity() {

    companion object {
        val preferenceConfig: PreferenceConfig = PreferenceConfig()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            findPreference<SwitchPreferenceCompat>("notify_all")?.onPreferenceChangeListener =
                Preference.OnPreferenceChangeListener { preference, newValue ->
                    println(" preference : $preference , newValue : $newValue")
                    preferenceConfig.notifyAll = newValue as Boolean
                    true
                }

            findPreference<SwitchPreferenceCompat>("sound")?.onPreferenceChangeListener =
                Preference.OnPreferenceChangeListener { preference, newValue ->
                    println(" preference : $preference , newValue : $newValue")
                    preferenceConfig.soundEnable = newValue as Boolean
                    true
                }
            findPreference<SwitchPreferenceCompat>("vibrate")?.onPreferenceChangeListener =
                Preference.OnPreferenceChangeListener { preference, newValue ->
                    println(" preference : $preference , newValue : $newValue")
                    preferenceConfig.vibrateEnable = newValue as Boolean
                    true
                }
        }
    }
}

data class PreferenceConfig(
    var notifyAll: Boolean = true,
    var soundEnable: Boolean = true,
    var vibrateEnable: Boolean = true
)