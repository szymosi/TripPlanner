<template>
  <div>
      <b-form>

          <label>Login</label>
          <b-input v-model="login" :state="loginValidation"></b-input>
                <b-form-invalid-feedback :state="loginValidation">
                Your login must be 4-50 characters long.
                </b-form-invalid-feedback>

          <label>Password</label>
          <b-input type="password" v-model="password" :state="passwordValidation"></b-input>
                <b-form-invalid-feedback :state="passwordValidation">
                Your password must be 8-50 characters long.
                </b-form-invalid-feedback>

          <label>Password Repeat</label>
          <b-input type="password" v-model="passwordRepeat" :state="passwordRepeatValidation"></b-input>
                <b-form-invalid-feedback :state="passwordRepeatValidation">
                Must be the same as password.
                </b-form-invalid-feedback>

          <label>Name</label>
          <b-input v-model="name"></b-input>

          <label>Surname</label>
          <b-input v-model="surname"></b-input>

          <label>{{message}}</label>
          <ul></ul>
          <b-button @click="register" block style="background-color: #687864;">Register</b-button>

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
        passwordRepeat: '',
        name: '',
        surname: '',
        message: ''
        };
    },
    computed:{
        loginValidation(){return this.login.length >= 4 && this.login.length <= 50},
        passwordValidation(){return this.password.length >= 8 && this.password.length <= 50},
        passwordRepeatValidation(){return this.password==this.passwordRepeat && this.password.length>0}
    },
    methods:{
        register: function(){
          this.$eventHub.$emit('Registered');

          axios.put(this.$url+'/User/Registration',{
          username: this.login,
          password: this.password,
          passwordRepeat: this.passwordRepeat,
          name: this.name,
          surname: this.surname
          
          }).then(response => {
            
            this.message="";
            this.$eventHub.$emit('Registered');
            
            this.$toasted.show('User '+response.data.username+' created', { 
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
          })
        }
    }
}
</script>