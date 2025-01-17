package com.nextcloud.android.sso.aidl;

import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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

public class ParcelFileDescriptorUtil {

    private ParcelFileDescriptorUtil() { }

    public static ParcelFileDescriptor pipeFrom(InputStream inputStream, IThreadListener listener)
            throws IOException {
        ParcelFileDescriptor[] pipe = ParcelFileDescriptor.createPipe();
        ParcelFileDescriptor readSide = pipe[0];
        ParcelFileDescriptor writeSide = pipe[1];

        // start the transfer thread
        new TransferThread(inputStream, new ParcelFileDescriptor.AutoCloseOutputStream(writeSide),
                listener)
                .start();

        return readSide;
    }

    public static ParcelFileDescriptor pipeTo(OutputStream outputStream, IThreadListener listener)
            throws IOException {
        ParcelFileDescriptor[] pipe = ParcelFileDescriptor.createPipe();
        ParcelFileDescriptor readSide = pipe[0];
        ParcelFileDescriptor writeSide = pipe[1];

        // start the transfer thread
        new TransferThread(new ParcelFileDescriptor.AutoCloseInputStream(readSide), outputStream,
                listener)
                .start();

        return writeSide;
    }

    static class TransferThread extends Thread {
        private final InputStream mIn;
        private final OutputStream mOut;
        private final IThreadListener mListener;

        TransferThread(InputStream in, OutputStream out, IThreadListener listener) {
            super("ParcelFileDescriptor Transfer Thread");
            mIn = in;
            mOut = out;
            mListener = listener;
            setDaemon(true);
        }

        @Override
        public void run() {
            byte[] buf = new byte[1024];
            int len;

            try {
                while ((len = mIn.read(buf)) > 0) {
                    mOut.write(buf, 0, len);
                }
                mOut.flush(); // just to be safe
            } catch(java.lang.NullPointerException e){
                try{
                    mOut.flush(); // just to be safe
                } catch (IOException eZ) {

                }
                Log.d("TransferThread", "as always: NullPointerException");
            } catch (IOException e) {
                Log.e("TransferThread", "writing failed", e);
            } finally {
                try {
                    mIn.close();
                } catch (IOException e) {
                    Log.e("TransferThread", "closing 'in' failed", e);
                }
                try {
                    mOut.close();
                } catch (IOException e) {
                    Log.e("TransferThread", "closing 'out' failed", e);
                }
            }
            if (mListener != null)
                mListener.onThreadFinished(this);
        }
    }
}
