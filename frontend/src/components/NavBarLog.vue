<template>
<div>
  <b-navbar toggleable="sm" type="dark"  style="background-color: #687864;">
    <b-navbar-brand href="#" @click="$router.push({path:'/'})">Trip Planner</b-navbar-brand>
    <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>



    <b-collapse id="nav-collapse" is-nav>
      <b-navbar-nav class="ml-auto">
        <b-nav-text style="color:#eef7e9">User: {{this.$store.getters.user.username}}</b-nav-text>&nbsp;
        <b-nav-item id="LogoutButton" @click="logout">Logout</b-nav-item>
      </b-navbar-nav>
    </b-collapse>
  </b-navbar> 
</div>
</template>

<script>
import axios from 'axios';
export default {
  created(){
    this.interval = setInterval(() => this.refreshToken(), 5*60*1000);
  },
  beforeDestroy(){
    clearInterval(this.interval);
  },
  methods:{
    logout: function(){
      this.$store.commit('clearStore');
    },
    refreshToken: function(){
      return axios.get(this.$url+'/User/RefreshToken',{
        headers:{
          Authorization: 'Bearer:'+this.$store.getters.token
        }
      }).then(response => {
        this.$store.commit('setToken', response.data);
      })
    }
  }
}
</script>