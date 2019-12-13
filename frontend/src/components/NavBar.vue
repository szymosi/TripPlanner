<template>
<div>
  <b-navbar toggleable="sm" type="dark"  style="background-color: #687864;">
    <b-navbar-brand href="#">Trip Planner</b-navbar-brand>

    <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

    <b-collapse id="nav-collapse" is-nav>
      <b-navbar-nav class="ml-auto">
        <b-nav-item id="LoginButton">Login</b-nav-item>
          <b-popover target="LoginButton" triggers="focus" placement="bottom">
            <Login></Login>
          </b-popover>
        <b-nav-item id="RegisterButton" >Register</b-nav-item>
          <b-popover ref="RegisterPopover" target="RegisterButton" triggers="focus" placement="bottom">
            <Register ref="RegisterPopover"></Register>
          </b-popover>
      </b-navbar-nav>
    </b-collapse>
  </b-navbar> 
</div>
</template>

<script>
import Login from '@/components/Login'
import Register from '@/components/Register'
//import Vue from 'vue'
export default {
    components: {
    Login, Register
  },
  methods:{
    Registered(){
      this.$root.$emit('bv::hide::popover')
    }
  },
  created(){
    this.$eventHub.$on('Registered', this.Registered);
  }
}
</script>