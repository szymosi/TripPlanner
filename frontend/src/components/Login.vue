<template>
  <div>
      <b-form>

           <label for="login">Login</label>
          <b-input id="login"></b-input>

          <label for="passowrd">Password</label>
          <b-input type="password" id="password"></b-input>

            <ul></ul>
           <b-button block style="background-color: #687864;">Login</b-button>

      </b-form>
  </div>
</template>

<script>
import axios from 'axios';
export default {
    data: function(){
        return{
        login: '',
        password: ''
        };
    },
    methods:{
        login: function(){
            this.$eventHub.$emit('Loggedin');
        return axios.put('http://192.168.1.166:8181/User/Login',{
          username: this.login,
          password: this.password
          })
        }
    }
}
</script>