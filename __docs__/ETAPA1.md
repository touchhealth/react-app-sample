# Apresentação

[Voltar](../README.md)

## 1. Setup do projeto

As ferramentas de inicialização, como o create-react-app, criam um ambiente puramente nodejs, que não combina muito com o estilo de aplicações que construimos. Em geral queremos que tudo fique dentro de um `.war`, a parte servidor e a parte cliente em javascript.

Para isso estamos acostumados a usar o maven como ferramenta principal de construção, e aqui vamos mostrar como compor tudo isso de uma forma que funcione bem, tanto para aplicativos, quanto para o uso em módulos dinâmicos (HEALS - dynmodules).

### Configurando Maven com SpringBoot 2.0

Requisitos:

* Maven 3.3.9+ e Java 8+ instalados e no PATH
* Os comandos apresentados devem ser executados em um terminal BASH (Linux)

Rode os comandos para iniciar o projeto git:

    mkdir react-app-sample
    cd react-app-sample
    git init

Crie o arquivo .gitignore com:

    .idea
    *.iml
    *.log
    node
    node_modules
    target
    src/main/webapp/app

Configurando o pom.xml para o spring boot e incluindo o plugin para o yarn:

~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <!-- 
    Assim como no HEALS, o Spring Boot é uma plataforma que tem módulos próprios e que gerencia um conjunto de dependências transitivas de terceiros. Para começar a usar essa plataforma importamos como parent o spring-boot-starter-parent
    -->
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.0.6.RELEASE</version>
    </parent>

    <groupId>br.com.touchhealth</groupId>
    <artifactId>react-app-sample</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>war</packaging>
    <description>Exemplo de aplicação Java Maven + Spring Boot + React Yarn</description>

    <!--  
    Aqui definimos a versão do groovy.
    Também definimos as versões de yarn e nodejs que serão instaladas localmente para esse build.
    -->
    <properties>
        <groovy_version>2.4.15</groovy_version>
        <node_version>v11.2.0</node_version>
        <yarn_version>v1.12.3</yarn_version>
    </properties>

    <dependencies>
        <!-- Dependência mais básica para aplicação web -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!-- Dependência para dar suporte a jpa -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <!-- Dependência para habilitar o reload automático da aplicação em desenvolvimento -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
        </dependency>
        <!-- Groovy com suporte a invoke dynamic -->
        <dependency>
            <groupId>org.codehaus.groovy</groupId>
            <artifactId>groovy-all</artifactId>
            <classifier>indy</classifier>
            <version>${groovy_version}</version>
        </dependency>
        <!-- Banco de dados em memória para o JPA -->
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <version>1.4.196</version>
        </dependency>
    </dependencies>

    <build>
        <finalName>react-app-sample</finalName>
        <resources>
            <resource>
                <directory>${basedir}/src/main/resources</directory>
                <filtering>true</filtering>
                <includes>
                    <include>banner.txt</include>
                </includes>
            </resource>
            <resource>
                <directory>${basedir}/src/main/resources</directory>
                <excludes>
                    <exclude>banner.txt</exclude>
                </excludes>
            </resource>
        </resources>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.7.0</version>
                <configuration>
                    <source>1.8</source>
                    <target>1.8</target>
                    <compilerId>groovy-eclipse-compiler</compilerId>
                    <compilerArguments>
                        <indy/>
                    </compilerArguments>
                </configuration>
                <dependencies>
                    <dependency>
                        <groupId>org.codehaus.groovy</groupId>
                        <artifactId>groovy-eclipse-compiler</artifactId>
                        <version>2.9.3-01</version>
                    </dependency>
                    <dependency>
                        <groupId>org.codehaus.groovy</groupId>
                        <artifactId>groovy-eclipse-batch</artifactId>
                        <version>2.4.15-02</version>
                    </dependency>
                </dependencies>
            </plugin>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <addResources>true</addResources>
                </configuration>
            </plugin>
            <!-- 
            Plugin que suporta o uso de NODE, NPM e YARN no maven
            -->
            <plugin>
                <groupId>com.github.eirslett</groupId>
                <artifactId>frontend-maven-plugin</artifactId>
                <version>1.6</version>
                <executions>
                    <execution>
                        <id>install node and yarn</id>
                        <goals>
                            <goal>install-node-and-yarn</goal>
                        </goals>
                        <phase>generate-resources</phase>
                        <configuration>
                            <nodeVersion>${node_version}</nodeVersion>
                            <yarnVersion>${yarn_version}</yarnVersion>
                        </configuration>
                    </execution>
                    <execution>
                        <id>yarn install</id>
                        <goals>
                            <goal>yarn</goal>
                        </goals>
                        <phase>generate-resources</phase>
                        <configuration>
                            <arguments>install</arguments>
                        </configuration>
                    </execution>
                    <execution>
                        <id>yarn build</id>
                        <goals>
                            <goal>yarn</goal>
                        </goals>
                        <phase>generate-resources</phase>
                        <configuration>
                            <arguments>build</arguments>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
    <!-- 
    Repositorio para os plugins relacionados ao groovy-compiler.
    Já está inserido no nexus da touch.
    -->
    <pluginRepositories>
        <pluginRepository>
            <id>bintray</id>
            <name>Groovy Bintray</name>
            <url>https://dl.bintray.com/groovy/maven</url>
            <releases>
                <updatePolicy>never</updatePolicy>
            </releases>
            <snapshots>
                <enabled>false</enabled>
            </snapshots>
        </pluginRepository>
    </pluginRepositories>
</project>
~~~

Com esse pom.xml pronto, vamos criar as estruturas de diretório padrão:

    Estrutura do projeto:
    react-app-sample
    |_ src/main/
      |_ java/        (Fontes java e groovy)
      |_ js/          (Fontes da aplicação javascript)
      |_ resources/   (Recursos não compiláveis)
      |_ webapp/      (Recursos estáticos da aplicação)
        |_ app/       (Diretório onde o bundle webpack será montado)

### Configurando Yarn e Webpack

Requisitos:

* Yarn instalado e no PATH
  
Pronto projeto maven está iniciado agora vamos configurar a parte Yarn e Webpack. Na raiz do projeto rode:

    yarn init

Respondendo as perguntas de forma específica:

    question private: true

Isso inicia o `package.json`. Agora usamos os comandos do yarn para adicionar as dependências.

~~~bash
# Instalando dependencias do projeto
yarn add react react-dom prop-types
yarn add bootstrap mobx mobx-react react-jss

# Instalando a nossa biblioteca de componentes
yarn add @touchhealth/react-components-sample

# Instalando babel os presets e os plugins
yarn add -D @babel/cli @babel/core @babel/polyfill  @babel/preset-env @babel/preset-react
yarn add -D @babel/plugin-proposal-class-properties @babel/plugin-proposal-decorators

# Instalando Webpack e seus loaders
yarn add -D webpack webpack-cli html-webpack-plugin rimraf
yarn add -D babel-loader css-loader file-loader react-svg-loader style-loader
~~~

Configuramos o `.babelrc`:

~~~json
{
  "presets": [
    "@babel/preset-env",
    "@babel/preset-react"
  ],
  "plugins": [
    ["@babel/plugin-proposal-decorators", { "legacy": true }],
    ["@babel/plugin-proposal-class-properties", { "loose" : true }]
  ]
}
~~~

Configuramos o `webpack.config.js`. Esse arquivo diz como o webpack, que é um bundler, gera os arquivos agrupados. Nesse caso, o arquivo de partida será o index.js dentro da pasta `src/main/js/`, e produzirá o bundle e os demais arquivos em `src/main/webapp/app/`. O babel será aplicado indiretamente através do `babel-loader`:

~~~javascript
const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin')

module.exports = {
    entry: './src/main/js/index.js',
    output: {
        filename: 'bundle-[hash].js',
        path: path.join(__dirname, 'src/main/webapp/app'),
    },
    resolve: {
        modules: [ "node_modules" ]
    },
    devtool: "source-map",
    module: {
        rules: [{
                test: /\.js$/,
                exclude: /(node_modules|bower_components)/,
                use: [ 'babel-loader' ]
            },
            {
                test: /\.css$/,
                use: [ 'style-loader', 'css-loader' ]
            },
            {
                test: /\.(png|svg|jpg|gif)$/,
                use: [ 'file-loader' ]
            },
            {
                test: /\.(woff|woff2|eot|ttf|otf)$/,
                use: [ 'file-loader' ]
            },
            {
                test: /\.(svgx)$/,
                use: [ 'react-svg-loader' ]
            }
        ]
    },
    optimization: {
        minimize: false,
        splitChunks: {
            chunks: 'all'
        }
    },
    plugins: [
        new HtmlWebpackPlugin({
            title: "react-app-sample",
            meta: {
                viewport: 'width=device-width, initial-scale=1, shrink-to-fit=no'
            },
            template: path.join(__dirname, 'src/main/js/_template.html')
        })
    ]
};
~~~

O arquivo `_template.html`, serve de base para o html final gerado para nossa aplicação. Nele são injetados os scripts necessários, com os devidos hashs gerados para o controle de cache eficiente:

~~~html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title><%= htmlWebpackPlugin.options.title %></title>
</head>
<body>
    <div id="react-app"></div>
</body>
</html>
~~~

Acrescentamos os scripts de build:

~~~json
    , "scripts": {
        "clean": "rimraf src/main/webapp/app && rimraf target/app-sample/app",
        "cleanall": "yarn clean && rimraf node_modules && rimraf node",
        "build": "yarn clean && webpack -p --progress",
        "dev": "yarn clean && webpack --watch --progress"
    }
~~~

### Configurando Webpack Dev Server

O Webpack Dev Server é uma ferramenta poderosa em relação a produtividade do desenvolvimento de uma aplicação react. Ela habilita um servidor no qual podemos rodar toda a parte javascript, e ainda se quisermos, podemos redirecionar as chamadas de servidor para o Tomcat:

Rodamos:

~~~bash
# Especificamos aqui a versão de dev-server porque a ultima 4 está em beta ainda.
yarn add -D webpack-merge webpack-dev-server@^3.0.0
~~~

E depois criamos uma configuração extendida `webpack.dev.js`:

~~~javascript
var merge = require('webpack-merge');
var baseConfig = require('./webpack.config.js');
var path = require('path');

module.exports = merge(baseConfig, {
    devServer: {
        // a base do conteúdo estatico carregado
        contentBase: path.join(__dirname, "src/main/webapp"),
        compress: true,
        // a porta do servidor de desenvolvimento
        port: 9000,
        // proxy especifica que caminhos serão redirecionados para o tomcat
        proxy: {
          "/api": {
            target: "http://localhost:8080"
          }
        },
        // a base do conteúdo gerado para a aplicação
        publicPath: "/app/"
    }
});
~~~

Agora é só acrescentar mais um script no `package.json`:

~~~json
    , "scripts": {
        //...
        "server": "yarn clean && webpack-dev-server --config webpack.dev.js"
    }
~~~

Com isso, temos a fundação feita, agora mãos a obra, vamos começar a construir...

[Próximo](ETAPA2.md)