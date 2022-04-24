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

package org.apache.shardingsphere.dbdiscovery.mysql.type.replication;

import org.apache.shardingsphere.dbdiscovery.spi.instance.type.IPPortPrimaryDatabaseInstance;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class MySQLNormalReplicationDatabaseDiscoveryProviderAlgorithmTest {
    
    @Test
    public void assertLoadHighlyAvailableStatus() throws SQLException {
        MySQLNormalReplicationHighlyAvailableStatus actual = new MySQLNormalReplicationMySQLDatabaseDiscoveryProviderAlgorithm().loadHighlyAvailableStatus(mockDataSource());
        assertThat(actual.getDatabaseInstance().toString(), is("127.0.0.1:3306"));
    }
    
    @Test
    public void assertFindPrimaryDataSource() throws SQLException {
        Optional<IPPortPrimaryDatabaseInstance> actual = new MySQLNormalReplicationMySQLDatabaseDiscoveryProviderAlgorithm().findPrimaryInstance("ds_0", mockDataSource());
        assertTrue(actual.isPresent());
        assertThat(actual.get().toString(), is("127.0.0.1:3306"));
    }
    
    private DataSource mockDataSource() throws SQLException {
        DataSource result = mock(DataSource.class, RETURNS_DEEP_STUBS);
        ResultSet resultSet = mock(ResultSet.class);
        when(result.getConnection().createStatement().executeQuery("SHOW SLAVE STATUS")).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString("Master_Host")).thenReturn("127.0.0.1");
        when(resultSet.getString("Master_Port")).thenReturn("3306");
        when(result.getConnection().getMetaData().getURL()).thenReturn("jdbc:mysql://127.0.0.1:3306/foo_ds");
        return result;
    }
}
