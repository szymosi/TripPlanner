<template>
<div>
  <v-tabs fixed-tabs show-arrows background-color="#a7b8a3">
    <v-tab>General</v-tab>
    <v-tab>Route</v-tab>
    <v-tab>Budget</v-tab>
    <v-tab>Tasks</v-tab>
    <v-tab>Blog</v-tab>

  <v-tab-item><General></General></v-tab-item>
  <v-tab-item><Route></Route></v-tab-item>  
  <v-tab-item><Budget></Budget></v-tab-item>
  <v-tab-item><Tasks></Tasks></v-tab-item>  
  <v-tab-item><Blog></Blog></v-tab-item>  
  </v-tabs>
</div>
</template>

<script>
import General from '@/components/Tabs/General'
import Route from '@/components/Tabs/Route'
import Budget from '@/components/Tabs/Budget'
import Tasks from '@/components/Tabs/Tasks'
import Blog from '@/components/Tabs/Blog'
export default {
  components:{
    General,
    Route,
    Budget,
    Tasks,
    Blog
  }
}
</script>

<style scoped>
</style>