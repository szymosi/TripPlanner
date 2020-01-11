<template>
<div>
  <b-nav tabs fill justified>
    <b-nav-item id="tabs">Active</b-nav-item>
    <b-nav-item id="tabs">Link</b-nav-item>
    <b-nav-item id="tabs">Link with a long name </b-nav-item>
    <b-nav-item id="tabs">Disabled</b-nav-item>
  </b-nav>
  <v-tabs
      fixed-tabs
      background-color="indigo"
      dark
    >
      <v-tab>
        Option
      </v-tab>
      <v-tab>
        Another Selection
      </v-tab>
      <v-tab>
        Items
      </v-tab>
      <v-tab>
        Another Screen
      </v-tab>
    </v-tabs>
</div>
</template>

<script>
export default {
  components:{
  }
}
</script>

<style scoped>
  #tabs
  {
    font-size:18px;
    color: #eef7e9;
  }
</style>