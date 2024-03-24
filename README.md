# Meeting organiser

A demo app for showing how to use [vault-maven-plugin](https://github.com/HomeOfTheWizard/vault-maven-plugin) in local environment of developers.  
A simple spring-boot application that needs some friend name setup in its applicaton.properties.  
The name of the friends is private for every person using the app, so it will be fetched from a Vault.  

For the demo you will need a Vault instance.  
If you do not have one already working, you can use a docker instance used for the integration tests of the maven plugin.  
Just download the [project](), and run `mvn docker:start`. It will initiate the Vault container.  

* ⚠️ The Vault is accessible via https through an nginx proxy. Its certificate is created for the hostname `nginx.docker`.  
  So in case you are on windows, you have to add an entry to your `hosts` file under `C:\Windows\System32\drivers\etc`.  
  When this is done, you will be able to communicate with this Vault via the hostname `nginx.docker`.
  This is as well the vault url setup in this demo by default, if you are using another Vault instance, you have to change the config in the pom.xml of this project.

* Also the docker instance setup for the plugin uses a [hardcoded admin token](https://github.com/HomeOfTheWizard/vault-maven-plugin/blob/1ee3a4f2f80efc4bebc3a7ea69b2f6a20820188b/pom.xml#L49) for authentication.
  If you want to use this vault instance, you should use this same token on this demo project to communicate with it.

# How to run the demo
The project is a basic spring app containing a bean for getting a simple Friend Object.  
The name of the friend is fetch from application properties. For privacy purpose we did not hardcode the name of the friend and push to git.  
Instead the application.yaml contains an environment variable (`testProperty`), and we will fetch it from a Vault instance, using the vault-maven-plugin.  
Before running the app, you must have in your Vault a kv secret stored for the friend name, with the key name `testKey`.  
You can either do it yourself, or use the mvn plugin to push it.   
But first be sure to configure your plugin correctly. In the pom.xml set correctly the URL and TOKEN for your Vault instance:  
```
<plugin>
    <groupId>com.homeofthewizard</groupId>
    <artifactId>vault-maven-plugin</artifactId>
    <version>1.1.4</version>
    <executions>
        <execution>
            <id>pull</id>
            <phase>initialize</phase>
            <goals>
                <goal>pull</goal>
            </goals>
        </execution>
    </executions>
    <configuration>
        <servers>
            <server>
                <url>https://nginx.docker:443</url>
                <token>${VAULT_TOKEN}</token>
                <paths>
                    <path>
                        <name>secret/data/myApp</name>
                        <mappings>
                            <mapping>
                                <key>testKey</key>
                                <property>testProperty</property>
                            </mapping>
                        </mappings>
                    </path>
                </paths>
            </server>
        </servers>
        <outputMethod>SystemProperties</outputMethod>
    </configuration>
</plugin>
```

if you want to push a value for the key to the Vault before running the app, uncomment the ligne 21 in the pom.xml,  
`<!--        <testProperty>Bob</testProperty>-->`  
and run `mvn vault:push -D"VAULT_TOKEN=XXXXXX"`.  

Now all is setup. You can simply run the project via intellij.
![image](https://github.com/HomeOfTheWizard/meeting-organiser/assets/38886889/fb934000-1b07-4882-975a-c58b2dbf1d62)
