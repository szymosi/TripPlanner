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
  <v-tab-item>4</v-tab-item>  
  <v-tab-item>5</v-tab-item>  
  </v-tabs>
</div>
</template>

<script>
import General from '@/components/Tabs/General'
import Route from '@/components/Tabs/Route'
import Budget from '@/components/Tabs/Budget'
export default {
  components:{
    General,
    Route,
    Budget
  }
}
</script>

<style scoped>
</style>