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
export default {
    data: function(){
        return{
        login: '',
        password: '',
        message: ''
        };
    },
    methods:{
        loginFunc: function(){

          axios.post(this.$url+'/User/Login',{
          username: this.login,
          password: this.password
          }).then(response => {
            this.message="";
            this.$eventHub.$emit('Loggedin',response.data);
            
            axios.defaults.headers.common['Authorization']='Bearer:'+response.data
            
            this.$toasted.show('User logged in', { 
            theme: "outline", 
            position: "top-right", 
            duration : 1000
          })
          })
          .catch(error=>{
              if(error.response){
                  this.message=error.response.data.message
              }
              else if(error.request){
                  this.message=error.request
              }
              else {
                  this.message=error
              }
            this.$toasted.show(this.message, { 
            theme: "outline", 
            position: "top-right", 
            duration : 1000
          })
          }) 
        }
    }
}
</script>