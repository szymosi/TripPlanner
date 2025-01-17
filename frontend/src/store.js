import Vuex from 'vuex';
import Vue from 'vue'

import createPersistedState from "vuex-persistedstate";

Vue.use(Vuex);

const state={

    user: null,
    token: "",
    trip: null,
    creatingTrip: false,
    participants: null,
    blog: null
   }

const getters={
       user: state=>state.user,
       token: state=>state.token,
       trip: state=>state.trip,
       creatingTrip: state=>state.creatingTrip,
       participants: state=>state.participants,
       blog: state=>state.blog
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
       },
       setCreatingTrip(state, creatingTrip){
           state.creatingTrip=creatingTrip
       },
       setParticipants(state, participants){
            state.participants=participants
       },
       setBlog(state, blog){
           state.blog=blog
       },
       clearStore(state){
        state.user=null,
        state.token="",
        state.trip=null,
        state.creatingTrip=null
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