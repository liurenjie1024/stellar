'use client';
import Image from 'next/image'
import styles from './page.module.css'

import React from 'react';
import { Input, Layout, Space } from 'antd';


const { Header, Footer, Sider, Content } = Layout;
// export default function Home() {
//   return (
//     <Layout>
//       <Sider>Sider</Sider>
//       <Layout>
//         <Header>Header</Header>
//         <Content>test</Content>
//         <Footer>Footer</Footer>
//       </Layout>
//     </Layout>
//   )
// }

export default function Home() {
  return (
    <Input placeholder="Basic usage" />
  )
}
