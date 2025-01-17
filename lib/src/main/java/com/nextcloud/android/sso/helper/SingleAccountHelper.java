/*
 * Nextcloud SingleSignOn
 *
 * @author David Luhmer
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.nextcloud.android.sso.helper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import com.nextcloud.android.sso.AccountImporter;
import com.nextcloud.android.sso.exceptions.NextcloudFilesAppAccountNotFoundException;
import com.nextcloud.android.sso.exceptions.NextcloudFilesAppAccountPermissionNotGrantedException;
import com.nextcloud.android.sso.exceptions.NextcloudFilesAppNotSupportedException;
import com.nextcloud.android.sso.exceptions.NoCurrentAccountSelectedException;
import com.nextcloud.android.sso.model.SingleSignOnAccount;

public final class SingleAccountHelper {

    private static final String PREF_CURRENT_ACCOUNT_STRING = "PREF_CURRENT_ACCOUNT_STRING";

    private SingleAccountHelper() {
    }

    @WorkerThread
    private static String getCurrentAccountName(Context context) throws NoCurrentAccountSelectedException {
        SharedPreferences mPrefs = AccountImporter.getSharedPreferences(context);
        String accountName = mPrefs.getString(PREF_CURRENT_ACCOUNT_STRING, null);
        if (accountName == null) {
            throw new NoCurrentAccountSelectedException(context);
        }
        return accountName;
    }

    @WorkerThread
    public static SingleSignOnAccount getCurrentSingleSignOnAccount(Context context)
            throws NextcloudFilesAppAccountNotFoundException, NoCurrentAccountSelectedException {
        return AccountImporter.getSingleSignOnAccount(context, getCurrentAccountName(context));
    }

    /**
     * Emits the currently set {@link SingleSignOnAccount} or <code>null</code> if an error occurred.
     */
    public static LiveData<SingleSignOnAccount> getCurrentSingleSignOnAccount$(@NonNull Context context) {
        return new SingleSignOnAccountLiveData(context, AccountImporter.getSharedPreferences(context), PREF_CURRENT_ACCOUNT_STRING);
    }

    /**
     * @deprecated Replaced by {@link #commitCurrentAccount(Context, String)}
     */
    @Deprecated(forRemoval = true)
    public static void setCurrentAccount(Context context, String accountName) {
        commitCurrentAccount(context, accountName);
    }

    /**
     * Warning: This call is <em>synchronous</em>.
     * Consider using {@link #applyCurrentAccount(Context, String)} if possible.
     */
    @SuppressLint("ApplySharedPref")
    @WorkerThread
    public static void commitCurrentAccount(Context context, String accountName) {
        AccountImporter.getSharedPreferences(context)
                .edit()
                .putString(PREF_CURRENT_ACCOUNT_STRING, accountName)
                .commit();
    }

    public static void applyCurrentAccount(Context context, String accountName) {
        AccountImporter.getSharedPreferences(context)
                .edit()
                .putString(PREF_CURRENT_ACCOUNT_STRING, accountName)
                .apply();
    }

    public static void reauthenticateCurrentAccount(Fragment fragment) throws NoCurrentAccountSelectedException, NextcloudFilesAppAccountNotFoundException, NextcloudFilesAppNotSupportedException, NextcloudFilesAppAccountPermissionNotGrantedException {
        AccountImporter.authenticateSingleSignAccount(fragment, getCurrentSingleSignOnAccount(fragment.getContext()));
    }

    public static void reauthenticateCurrentAccount(Activity activity) throws NoCurrentAccountSelectedException, NextcloudFilesAppAccountNotFoundException, NextcloudFilesAppNotSupportedException, NextcloudFilesAppAccountPermissionNotGrantedException {
        AccountImporter.authenticateSingleSignAccount(activity, getCurrentSingleSignOnAccount(activity));
    }

    /**
     * @deprecated Use {@link #getCurrentSingleSignOnAccount$(Context)} which is lifecycle aware
     */
    @Deprecated(forRemoval = true)
    public static void registerSharedPreferenceChangeListener(Context context, 
                                                              SharedPreferences.OnSharedPreferenceChangeListener listener) {
        AccountImporter.getSharedPreferences(context)
                .registerOnSharedPreferenceChangeListener(listener);
    }

    /**
     * @deprecated Use {@link #getCurrentSingleSignOnAccount$(Context)} which is lifecycle aware
     */
    @Deprecated(forRemoval = true)
    public static void unregisterSharedPreferenceChangeListener(Context context,
                                                                SharedPreferences.OnSharedPreferenceChangeListener listener) {
        AccountImporter.getSharedPreferences(context)
                .unregisterOnSharedPreferenceChangeListener(listener);
        
    }
}
