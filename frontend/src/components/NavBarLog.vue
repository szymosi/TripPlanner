<template>
  <div>
    <b-navbar toggleable="sm" type="dark" style="background-color: #687864;">
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
export default {
  methods: {
    logout() {
      this.$store.commit("clearStore");
    }
  }
};
</script>