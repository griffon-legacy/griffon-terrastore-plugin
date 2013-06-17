
Terrastore support
------------------

Plugin page: [http://artifacts.griffon-framework.org/plugin/terrastore](http://artifacts.griffon-framework.org/plugin/terrastore)


The Terrastore plugin enables lightweight access to [Terrastore][1] databases.
This plugin does NOT provide domain classes nor dynamic finders like GORM does.

Usage
-----
Upon installation the plugin will generate the following artifacts in `$appdir/griffon-app/conf`:

 * TerrastoreConfig.groovy - contains the database definitions.
 * BootstrapTerrastore.groovy - defines init/destroy hooks for data to be manipulated during app startup/shutdown.

A new dynamic method named `withTerrastore` will be injected into all controllers,
giving you access to a `terrastore.client.TerrastoreClient` object, with which you'll be able
to make calls to the database. Remember to make all database calls off the EDT
otherwise your application may appear unresponsive when doing long computations
inside the EDT.

This method is aware of multiple databases. If no clientName is specified when calling
it then the default database will be selected. Here are two example usages, the first
queries against the default database while the second queries a database whose name has
been configured as 'internal'

    package sample
    class SampleController {
        def queryAllDatabases = {
            withTerrastore { clientName, client -> ... }
            withTerrastore('internal') { clientName, client -> ... }
        }
    }

This method is also accessible to any component through the singleton `griffon.plugins.terrastore.TerrastoreConnector`.
You can inject these methods to non-artifacts via metaclasses. Simply grab hold of a particular metaclass and call
`TerrastoreEnhancer.enhance(metaClassInstance, terrastoreProviderInstance)`.

Configuration
-------------
### Dynamic method injection

The `withTerrastore()` dynamic method will be added to controllers by default. You can
change this setting by adding a configuration flag in `griffon-app/conf/Config.groovy`

    griffon.terrastore.injectInto = ['controller', 'service']

### Events

The following events will be triggered by this addon

 * TerrastoreConnectStart[config, clientName] - triggered before connecting to the database
 * TerrastoreConnectEnd[clientName, client] - triggered after connecting to the database
 * TerrastoreDisconnectStart[config, clientName, client] - triggered before disconnecting from the database
 * TerrastoreDisconnectEnd[config, clientName] - triggered after disconnecting from the database

### Multiple Stores

The config file `TerrastoreConfig.groovy` defines a default client block. As the name
implies this is the client used by default, however you can configure named clients
by adding a new config block. For example connecting to a client whose name is 'internal'
can be done in this way

    databases {
        internal {
            host = 'http://localhost:8090'
        }
    }

This block can be used inside the `environments()` block in the same way as the
default client block is used.

### Example

A trivial sample application can be found at [https://github.com/aalmiray/griffon_sample_apps/tree/master/persistence/terrastore][2]

Testing
-------
The `withTerrastore()` dynamic method will not be automatically injected during unit testing, because addons are simply not initialized
for this kind of tests. However you can use `TerrastoreEnhancer.enhance(metaClassInstance, terrastoreProviderInstance)` where 
`terrastoreProviderInstance` is of type `griffon.plugins.terrastore.TerrastoreProvider`. The contract for this interface looks like this

    public interface TerrastoreProvider {
        Object withTerrastore(Closure closure);
        Object withTerrastore(String clientName, Closure closure);
        <T> T withTerrastore(CallableWithArgs<T> callable);
        <T> T withTerrastore(String clientName, CallableWithArgs<T> callable);
    }

It's up to you define how these methods need to be implemented for your tests. For example, here's an implementation that never
fails regardless of the arguments it receives

    class MyTerrastoreProvider implements TerrastoreProvider {
        Object withTerrastore(String clientName = 'default', Closure closure) { null }
        public <T> T withTerrastore(String clientName = 'default', CallableWithArgs<T> callable) { null }
    }

This implementation may be used in the following way

    class MyServiceTests extends GriffonUnitTestCase {
        void testSmokeAndMirrors() {
            MyService service = new MyService()
            TerrastoreEnhancer.enhance(service.metaClass, new MyTerrastoreProvider())
            // exercise service methods
        }
    }


[1]: http://code.google.com/p/terrastore/
[2]: https://github.com/aalmiray/griffon_sample_apps/tree/master/persistence/terrastore

