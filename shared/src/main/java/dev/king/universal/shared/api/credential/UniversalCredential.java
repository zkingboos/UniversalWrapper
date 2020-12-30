/*
 * Copyright (c) 2020 yking-projects
 */

package dev.king.universal.shared.api.credential;

/**
 * Api credential
 *
 * @author ykingboos
 */
public interface UniversalCredential {

    /**
     * Get the hostname to desired host
     *
     * @return raw name of hostname
     */
    String getHostname();

    /**
     * Get the database to desired database name
     *
     * @return raw name of database
     */
    String getDatabase();

    /**
     * Get the user to authenticate with mysql server
     *
     * @return raw name of user
     */
    String getUser();

    /**
     * Get the password to authenticate with mysql server
     *
     * @return raw name of password
     */
    String getPassword();
}
