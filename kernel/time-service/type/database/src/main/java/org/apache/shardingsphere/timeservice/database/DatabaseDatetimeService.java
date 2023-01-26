/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.timeservice.database;

import org.apache.shardingsphere.timeservice.database.config.DatabaseDatetimeServiceConfiguration;
import org.apache.shardingsphere.timeservice.database.exception.DatetimeLoadingException;
import org.apache.shardingsphere.timeservice.database.provider.DatetimeLoadingSQLProvider;
import org.apache.shardingsphere.infra.database.type.DatabaseTypeEngine;
import org.apache.shardingsphere.timeservice.spi.ShardingSphereTimeService;
import org.apache.shardingsphere.infra.util.spi.type.typed.TypedSPIRegistry;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Database datetime service.
 */
public final class DatabaseDatetimeService implements ShardingSphereTimeService {
    
    private final DatabaseDatetimeServiceConfiguration timeServiceConfig = DatabaseDatetimeServiceConfiguration.getInstance();
    
    @Override
    public Date getDatetime() {
        try {
            return loadDatetime(timeServiceConfig.getDataSource(),
                    TypedSPIRegistry.getService(DatetimeLoadingSQLProvider.class, DatabaseTypeEngine.getTrunkDatabaseTypeName(timeServiceConfig.getStorageType())).getDatetimeLoadingSQL());
        } catch (final SQLException ex) {
            throw new DatetimeLoadingException(ex);
        }
    }
    
    private Date loadDatetime(final DataSource dataSource, final String datetimeLoadingSQL) throws SQLException {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(datetimeLoadingSQL)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                return (Date) resultSet.getObject(1);
            }
        }
    }
    
    @Override
    public String getType() {
        return "Database";
    }
}