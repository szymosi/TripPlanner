import Vue from 'vue'
import App from './App.vue'
import router from './router'
import Toasted from 'vue-toasted';

import store from '@/store'

import * as VueGoogleMaps from "vue2-google-maps";

import Vuetify from 'vuetify/lib'

import BootstrapVue from 'bootstrap-vue'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import vuetify from './plugins/vuetify';

Vue.use(VueGoogleMaps, {
  load: {
    key: "AIzaSyD7tKWjIs6wYOmFHWmcC8-X-m7gOD-kLDU",
    libraries: "places"
  }
});

Vue.use(Vuetify)
Vue.use(BootstrapVue)
Vue.use(Toasted)

Vue.prototype.$eventHub = new Vue();

Vue.config.productionTip = false

new Vue({
  vuetify,
  store,
  router,
  render: h => h(App)
}).$mount('#app')

