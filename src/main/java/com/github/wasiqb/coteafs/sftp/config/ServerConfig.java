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
package com.github.wasiqb.coteafs.sftp.config;

/**
 * @author wasiqb
 * @since Mar 21, 2019
 */
public class ServerConfig {
	private String	host;
	private String	keyPath;
	private String	pass;
	private int		port;
	private String	user;

	/**
	 * @author wasiqb
	 * @since Mar 21, 2019
	 * @return the host
	 */
	public String getHost () {
		return this.host;
	}

	/**
	 * @author wasiqb
	 * @since Mar 21, 2019
	 * @return the keyPath
	 */
	public String getKeyPath () {
		return this.keyPath;
	}

	/**
	 * @author wasiqb
	 * @since Mar 21, 2019
	 * @return the pass
	 */
	public String getPass () {
		return this.pass;
	}

	/**
	 * @author wasiqb
	 * @since Mar 21, 2019
	 * @return the port
	 */
	public int getPort () {
		return this.port;
	}

	/**
	 * @author wasiqb
	 * @since Mar 21, 2019
	 * @return the user
	 */
	public String getUser () {
		return this.user;
	}

	/**
	 * @author wasiqb
	 * @since Mar 21, 2019
	 * @param host
	 *            the host to set
	 */
	public void setHost (final String host) {
		this.host = host;
	}

	/**
	 * @author wasiqb
	 * @since Mar 21, 2019
	 * @param keyPath
	 *            the keyPath to set
	 */
	public void setKeyPath (final String keyPath) {
		this.keyPath = keyPath;
	}

	/**
	 * @author wasiqb
	 * @since Mar 21, 2019
	 * @param pass
	 *            the pass to set
	 */
	public void setPass (final String pass) {
		this.pass = pass;
	}

	/**
	 * @author wasiqb
	 * @since Mar 21, 2019
	 * @param port
	 *            the port to set
	 */
	public void setPort (final int port) {
		this.port = port;
	}

	/**
	 * @author wasiqb
	 * @since Mar 21, 2019
	 * @param user
	 *            the user to set
	 */
	public void setUser (final String user) {
		this.user = user;
	}
}