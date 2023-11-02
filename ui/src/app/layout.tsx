'use client';

import type { Metadata } from 'next'
import { Inter } from 'next/font/google'
import './globals.css'
import React from 'react';
import { Layout, Space, } from 'antd';
import { AppstoreFilled } from '@ant-design/icons';

const inter = Inter({ subsets: ['latin'] })


const { Header, Footer, Sider, Content } = Layout;

const headerStyle: React.CSSProperties = {
  textAlign: 'center',
  padding: 0,
  color: '#fff',
  backgroundColor: '#7dbcea',
};

const contentStyle: React.CSSProperties = {
  textAlign: 'center',
  color: '#fff',
  backgroundColor: '#108ee9',
  margin: '24px 16px 0',
  overflow: 'initial'
};

const siderStyle: React.CSSProperties = {
  overflow: 'auto',
  height: '100vh',
  position: 'fixed',
  left: 0,
  top: 0,
  bottom: 0,
  textAlign: 'center',
  color: '#fff',
  backgroundColor: '#3ba0e9',
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <html lang="en">
      <body >
        <Layout>
          <Sider style={siderStyle}>
            <AppstoreFilled />
          </Sider>
          <Layout className="site-layout" style={{ marginLeft: 200 }}>
            <Header style={headerStyle} >Header</Header>
            <Content>{children}</Content>
          </Layout>
        </Layout>
      </body>
    </html>
  )
}
