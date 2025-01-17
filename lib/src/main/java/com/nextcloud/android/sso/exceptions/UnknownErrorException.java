/*
 *  Nextcloud SingleSignOn
 *
 *  @author David Luhmer
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

package com.nextcloud.android.sso.exceptions;

import com.nextcloud.android.sso.R;

public class UnknownErrorException extends SSOException {

    /**
     * @deprecated Use {@link #UnknownErrorException(String)}
     */
    @Deprecated(forRemoval = true)
    public UnknownErrorException() {
        super();
    }

    public UnknownErrorException(String message) {
        super(message, R.string.unknown_error_title);
    }
}
