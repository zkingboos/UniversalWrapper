package dev.king.universal.api.mysql;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * The credentials of mysql
 *
 * @author zkingboos_
 */
@Getter
@Builder
@RequiredArgsConstructor
public final class UniversalCredentials {
    private final String hostname, database, user, password;
}