export default new Vuex.Store({
    state:{
     // Current state of the application lies here.
     user:{
        id: null,
        username: null,
        name: null,
        surname: null
     },
    },
    getters:{
     // Compute derived state based on the current state. More like computed property.
    },
    mutations:{
     // Mutate the current state
    },
    actions:{
     // Get data from server and send that to mutations to mutate the current state
    }
   })