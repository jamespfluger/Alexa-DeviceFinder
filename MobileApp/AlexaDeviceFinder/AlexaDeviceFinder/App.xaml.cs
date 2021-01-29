﻿using System;
using DeviceFinder;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace DeviceFinder
{
    public partial class App : Application
    {
        public App()
        {
            InitializeComponent();

            MainPage = new DeviceConfigPage();
        }

        protected override void OnStart()
        {
        }

        protected override void OnSleep()
        {
        }

        protected override void OnResume()
        {
        }
    }
}
