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

package org.apache.shardingsphere.infra.database.type.dialect;

import com.google.common.collect.Sets;
import org.apache.shardingsphere.infra.database.metadata.dialect.MySQLDataSourceMetaData;
import org.apache.shardingsphere.infra.database.type.DatabaseType;
import org.apache.shardingsphere.sql.parser.sql.common.constant.QuoteCharacter;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Database type of MySQL.
 */
public final class MySQLDatabaseType implements DatabaseType {
    
    @Override
    public String getName() {
        return "MySQL";
    }
    
    @Override
    public QuoteCharacter getQuoteCharacter() {
        return QuoteCharacter.BACK_QUOTE;
    }
    
    @Override
    public Collection<String> getJdbcUrlPrefixes() {
        return Arrays.asList("jdbc:mysql:", "jdbc:mysqlx:");
    }
    
    @Override
    public MySQLDataSourceMetaData getDataSourceMetaData(final String url, final String username) {
        return new MySQLDataSourceMetaData(url);
    }
    
    @Override
    public Optional<String> getDataSourceClassName() {
        return Optional.of("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
    }
    
    @Override
    public Map<String, Collection<String>> getSystemSchemas() {
        Map<String, Collection<String>> result = new LinkedHashMap<>();
        result.put("information_schema", Sets.newHashSet("information_schema"));
        result.put("performance_schema", Sets.newHashSet("performance_schema"));
        result.put("mysql", Sets.newHashSet("mysql"));
        result.put("sys", Sets.newHashSet("sys"));
        return result;
    }
}
