package com.nextcloud.android.sso.model;

import com.nextcloud.android.sso.exceptions.SSOException;

import java.io.Serializable;

/**
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

/**
 * @deprecated Construct {@link SSOException} directly providing message and title.
 */
@Deprecated(forRemoval = true)
public class ExceptionMessage implements Serializable {

    public String title;
    public String message;

    public ExceptionMessage(String title, String message) {
        this.title = title;
        this.message = message;
    }

}
