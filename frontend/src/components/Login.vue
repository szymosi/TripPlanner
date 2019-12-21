<template>
  <div>
      <b-form>
           <label>Login</label>
          <b-input v-model="login" id="login"></b-input>

          <label>Password</label>
          <b-input v-model="password" type="password" id="password"></b-input>

            <ul></ul>
           <b-button @click="loginFunc" style="background-color: #687864;">Login</b-button>
      </b-form>
  </div>
</template>

<script>
import axios from 'axios';
import {mapMutations} from 'vuex';
export default {
    data: function(){
        return{
        login: '',
        password: '',
        };
    },
    methods:{
        ...mapMutations(['setUserDetails']),
        loginFunc: function(){
          this.setUserDetails();
          //this.$store.commit('setUser');

          return axios.post('http://localhost:8181/User/Login',{
          username: this.login,
          password: this.password
          }).then(response => {
            this.$eventHub.$emit('Loggedin');

            this.$toasted.show('User '+response.data.username+' logged in', { 
            theme: "outline", 
            position: "top-right", 
            duration : 1000
          })
          })
          .catch(error=>{
            this.$toasted.show(error.response.data.message, { 
            theme: "outline", 
            position: "top-right", 
            duration : 1000
          })
          })
        }
    }
}
</script>