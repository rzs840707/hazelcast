/*
 * Copyright (c) 2008-2015, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.client.impl.protocol.parameters;

import com.hazelcast.client.impl.protocol.ClientMessage;
import com.hazelcast.client.impl.protocol.ClientMessageType;
import com.hazelcast.client.impl.protocol.util.ParameterUtil;
import com.hazelcast.nio.serialization.Data;
import com.hazelcast.nio.serialization.DefaultData;

/**
 * Sample Put parameter
 */
@edu.umd.cs.findbugs.annotations.SuppressWarnings({"URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD"})
public class GenericResultParameters {

    /**
     * ClientMessageType of this message
     */
    public static final ClientMessageType TYPE = ClientMessageType.RESULT;
    public Data result;

    private GenericResultParameters(ClientMessage flyweight) {
        Data data = flyweight.getData();
        result = data.equals(DefaultData.NULL_DATA) ? null : data;
    }

    public static GenericResultParameters decode(ClientMessage flyweight) {
        return new GenericResultParameters(flyweight);
    }

    public static ClientMessage encode(Data result) {
        result = result == null ? DefaultData.NULL_DATA : result;

        final int requiredDataSize = calculateDataSize(result);
        ClientMessage clientMessage = ClientMessage.createForEncode(requiredDataSize);
        clientMessage.setMessageType(TYPE.id());
        clientMessage.set(result);
        clientMessage.updateFrameLength();
        return clientMessage;
    }

    /**
     * sample data size estimation
     *
     * @return size
     */
    public static int calculateDataSize(Data result) {
        return ClientMessage.HEADER_SIZE + ParameterUtil.calculateDataSize(result);
    }

}
