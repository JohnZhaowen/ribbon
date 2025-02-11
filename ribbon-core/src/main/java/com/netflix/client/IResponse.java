/*
*
* Copyright 2013 Netflix, Inc.
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
*
*/
package com.netflix.client;

import java.io.Closeable;
import java.net.URI;
import java.util.Map;

/**
 * Response interface for the client framework.  
 *
 */
public interface IResponse extends Closeable
{
   
   /**
    * Returns the raw entity if available from the response 
    */
   Object getPayload() throws ClientException;
      
   /**
    * A "peek" kinda API. Use to check if your service returned a response with an Entity
    */
   boolean hasPayload();
   
   /**
    * @return true if the response is deemed success, for example, 200 response code for http protocol.
    */
   boolean isSuccess();
   
      
   /**
    * Return the Request URI that generated this response
    */
   URI getRequestedURI();
   
   /**
    * 
    * @return Headers if any in the response.
    */
   Map<String, ?> getHeaders();
}
