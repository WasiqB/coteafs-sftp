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

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.wasiqb.coteafs.config.loader.ConfigLoader;
import com.github.wasiqb.coteafs.sftp.config.ServerConfig;
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
	private static final ServerConfig	CONFIG;
	private static final Logger			LOG;
	private static Session				session;

	static {
		LOG = LogManager.getLogger (RemoteServer.class);
		CONFIG = ConfigLoader.settings ()
			.withKey ("coteafs.sftp.config")
			.withDefault ("/sftp-config.yaml")
			.load (ServerConfig.class);
	}

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
		LOG.info ("Copying file from Remote server to Local machine...");
		LOG.trace (format ("From remote path: [%s] To Local path: [%s]...", remote, local));
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
		LOG.info ("Copying file to Remote server from Local machine...");
		LOG.trace (
			format ("From Local path: [%s] To Remote path: [%s]...", local.getPath (), remoteDir));
		createSession ();
		final ChannelSftp channel = (ChannelSftp) session.openChannel ("sftp");
		channel.connect ();
		try (FileInputStream in = new FileInputStream (local)) {
			channel.cd (remoteDir);
			channel.put (in, local.getName ());
			channel.disconnect ();
		}
		catch (final IOException e) {
			LOG.error ("Error occurred while copying file to remote server...");
			LOG.catching (e);
		}
		finally {
			session.disconnect ();
		}
	}

	private static void createSession () throws JSchException {
		LOG.info ("Creating session on SFTP Server...");
		final String path = CONFIG.getKeyPath ();
		final String user = requireNonNull (CONFIG.getUser (), "SSH User name is required.");
		final String pass = requireNonNull (CONFIG.getPass (), "SSH key pass is required.");
		final String domain = requireNonNull (CONFIG.getHost (), "Server Host is required.");
		final int port = CONFIG.getPort ();

		final JSch sch = new JSch ();
		if (isNotEmpty (path)) {
			sch.addIdentity (path, pass);
		}
		final Properties config = new java.util.Properties ();
		config.put ("StrictHostKeyChecking", "no");

		session = sch.getSession (user, domain, port);
		if (isEmpty (path)) {
			session.setPassword (pass);
		}
		session.setConfig (config);
		session.connect ();
	}
}