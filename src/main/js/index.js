import '@babel/polyfill'
import 'bootstrap/dist/css/bootstrap.min.css'

import Logo from './LogoTouchHealth.svgx'

import React from 'react'
import ReactDOM from 'react-dom'

import Theme from '@touchhealth/react-components-sample/Theme'
import Tab from '@touchhealth/react-components-sample/TabThemed'

ReactDOM.render(
<Theme theme={{primaryColor: 'red'}}>
    <div style={{padding: '20px'}}>
        <Logo width={150} height={40}></Logo>
        <h1>Teste da App React</h1>
        <Tab
            titles={['Pag1', 'Pag2']}
            content={[ 
                <div>content1</div>
                ,
                <div>content2</div>
            ]}
        />
    </div>
</Theme>    
, document.getElementById('react-app'))