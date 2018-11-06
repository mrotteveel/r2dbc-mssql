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

package io.r2dbc.mssql.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.r2dbc.mssql.message.type.Length;
import io.r2dbc.mssql.message.type.TypeInformation;
import io.r2dbc.mssql.message.type.TypeInformation.SqlServerType;

/**
 * Codec for floating-point values that are represented as {@link Float}.
 *
 * <ul>
 * <li>Server types: {@link SqlServerType#FLOAT} (8-byte) and  {@link SqlServerType#REAL} (4-byte)</li>
 * <li>Java type: {@link Float}</li>
 * <li>Downcast: to {@link Float}</li>
 * </ul>
 *
 * @author Mark Paluch
 */
final class FloatCodec extends AbstractCodec<Float> {

    public static final FloatCodec INSTANCE = new FloatCodec();

    private FloatCodec() {
        super(Float.class);

    }

    @Override
    boolean doCanDecode(TypeInformation typeInformation) {
        return DoubleCodec.INSTANCE.doCanDecode(typeInformation);
    }

    @Override
    Float doDecode(ByteBuf buffer, Length length, TypeInformation type, Class<? extends Float> valueType) {

        Double value = DoubleCodec.INSTANCE.doDecode(buffer, length, type, Double.class);
        return value == null ? null
            : value.floatValue();
    }

    @Override
    Encoded doEncode(ByteBufAllocator allocator, TypeInformation type, Float value) {
        return DoubleCodec.INSTANCE.doEncode(allocator, type, value.doubleValue());
    }
}
