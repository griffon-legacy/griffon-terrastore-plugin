/*
 * Copyright 2012 the original author or authors.
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

package griffon.plugins.terrastore

import griffon.util.CallableWithArgs
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Andres Almiray
 */
final class TerrastoreEnhancer {
    private static final Logger LOG = LoggerFactory.getLogger(TerrastoreEnhancer)

    private TerrastoreEnhancer() {}

    static void enhance(MetaClass mc, TerrastoreProvider provider = TerrastoreClientHolder.instance) {
        if(LOG.debugEnabled) LOG.debug("Enhancing $mc with $provider")
        mc.withTerrastore = {Closure closure ->
            provider.withTerrastore('default', closure)
        }
        mc.withTerrastore << {String clientName, Closure closure ->
            provider.withTerrastore(clientName, closure)
        }
        mc.withTerrastore << {CallableWithArgs callable ->
            provider.withTerrastore('default', callable)
        }
        mc.withTerrastore << {String clientName, CallableWithArgs callable ->
            provider.withTerrastore(clientName, callable)
        }
    }
}
