import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

export default new Vuex.Store({
    state:{
     user: 'default'
    },
    getters:{
        user: state=>state.user

    },
    mutations:{
        setUser(state){
            state.user='123456789'
        }

    },
    actions:{

    }
   })