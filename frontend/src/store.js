import Vuex from 'vuex';
import Vue from 'vue'

import createPersistedState from "vuex-persistedstate";

Vue.use(Vuex);

const state={
    user: null,
    token: "",
    trip: null
   }

const getters={
       user: state=>state.user,
       token: state=>state.token,
       trip: state=>state.trip
   }

const mutations={
       setUserDetails(state, user){
           state.user=user
       },
       setToken(state, token){
           state.token=token
       },
       setTrip(state, trip){
           state.trip=trip
       }
   }

const actions={

   }

   export default new Vuex.Store({
    state,
    getters,
    mutations,
    actions,
    
    plugins: [createPersistedState({
      storage: window.sessionStorage,
  })],
})