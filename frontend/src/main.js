import Vue from 'vue'
import App from './App.vue'
import router from './router'
import Toasted from 'vue-toasted';

import store from './store'

import BootstrapVue from 'bootstrap-vue'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'


Vue.use(BootstrapVue)
Vue.use(Toasted)

Vue.prototype.$eventHub = new Vue();

Vue.config.productionTip = false

new Vue({
  store,
  router,
  render: h => h(App)
}).$mount('#app')

