/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.r2dbc.mssql;

import io.r2dbc.mssql.message.TransactionDescriptor;
import io.r2dbc.mssql.message.header.HeaderOptions;
import io.r2dbc.mssql.message.header.Status;
import io.r2dbc.mssql.message.header.Type;
import io.r2dbc.mssql.message.token.RpcRequest;
import io.r2dbc.mssql.message.type.Collation;
import io.r2dbc.mssql.util.ClientMessageAssert;
import io.r2dbc.mssql.util.HexUtils;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link CursoredQueryMessageFlow}.
 *
 * @author Mark Paluch
 */
class CursoredQueryMessageFlowUnitTests {

    @Test
    void shouldEncodeSpCursorOpen() throws Exception {

        Collation collation = Collation.from(13632521, 52);

        RpcRequest rpcRequest = CursoredQueryMessageFlow.spCursorOpen("SELECT * FROM my_table", collation, TransactionDescriptor.empty());

        String hex = "16 00 00 00 12 00 00 00" +
            "02 00 00 00 00 00 00 00 00 00 01 00 00 00 FF FF" +
            "02 00 00 00 00 01 26 04 04 00 00 00 00 00 00 E7" +
            "40 1F 09 04 D0 00 34 2C 00 53 00 45 00 4C 00 45" +
            "00 43 00 54 00 20 00 2A 00 20 00 46 00 52 00 4F" +
            "00 4D 00 20 00 6D 00 79 00 5F 00 74 00 61 00 62" +
            "00 6C 00 65 00 00 00 26 04 04 10 00 00 00 00 00" +
            "26 04 04 01 20 00 00 00 01 26 04 04 00 00 00 00";

        ClientMessageAssert.assertThat(rpcRequest).encoded()
            .hasHeader(HeaderOptions.create(Type.RPC, Status.empty()))
            .isEncodedAs(expected -> {

                expected.writeBytes(HexUtils.decodeToByteBuf(hex));
            });
    }
}
