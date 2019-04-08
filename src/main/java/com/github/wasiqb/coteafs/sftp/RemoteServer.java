/**
 * Copyright (c) 2017-2020, Wasiq Bhamla.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.wasiqb.coteafs.sftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * @author wasiqb
 * @since Mar 21, 2019
 */
public class RemoteServer {
	private static Session session;

	/**
	 * @author wasiqb
	 * @since Apr 7, 2019
	 * @param remote
	 * @param local
	 * @throws JSchException
	 * @throws SftpException
	 */
	public static void copyFrom (final String remote, final String local)
		throws JSchException, SftpException {
		createSession ();
		final ChannelSftp channel = (ChannelSftp) session.openChannel ("sftp");
		channel.connect ();
		channel.get (remote, local);
		channel.disconnect ();
		session.disconnect ();
	}

	/**
	 * @author wasiqb
	 * @since Apr 7, 2019
	 * @param local
	 * @param remoteDir
	 * @throws JSchException
	 * @throws SftpException
	 */
	public static void copyTo (final File local, final String remoteDir)
		throws JSchException, SftpException {
		createSession ();
		final ChannelSftp channel = (ChannelSftp) session.openChannel ("sftp");
		channel.connect ();
		try (FileInputStream in = new FileInputStream (local)) {
			channel.cd (remoteDir);
			channel.put (in, local.getName ());
			channel.disconnect ();
		}
		catch (final IOException e) {
			e.printStackTrace ();
		}
		finally {
			session.disconnect ();
		}
	}

	private static void createSession () throws JSchException {
		final String path = getAppConfig (SSH_PATH);
		final String pass = requireNonNull (getAppConfig (SSH_PASS), "SSH key pass is required.");
		final String user = requireNonNull (getAppConfig (SSH_USER), "SSH User name is required.");
		final String domain = requireNonNull (getAppConfig (TEST_DOMAIN),
			"Server Host is required.");
		final int port = Integer
			.parseInt (requireNonNull (getAppConfig (SFTP_PORT, "22"), "Port is required"));

		final JSch sch = new JSch ();
		if (StringUtils.isNotEmpty (path)) {
			sch.addIdentity (path, pass);
		}
		final Properties config = new java.util.Properties ();
		config.put ("StrictHostKeyChecking", "no");

		session = sch.getSession (user, domain, port);
		if (StringUtils.isEmpty (path)) {
			session.setPassword (pass);
		}
		session.setConfig (config);
		session.connect ();
	}
}