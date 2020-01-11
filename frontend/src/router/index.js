import Vue from 'vue'
import axios from 'axios'
import VueAxios from 'vue-axios'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import Trip from '../views/Trip'

Vue.use(VueRouter)
Vue.use(VueAxios, axios)

const routes = [
  {
    path: '/',
    name: 'home',
    component: Home
  },
  {
    path: '/trip',
    name: 'trip',
    component: Trip
  }
]

const router = new VueRouter({
  routes
})

export default router
