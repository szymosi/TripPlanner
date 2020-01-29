<template>
<div>
  <div id="list"><TripsList/></div>
  <div id="content" style="height: 100%">
    <Trip v-if="!this.$store.getters.creatingTrip"></Trip>
    <TripCreate v-if="this.$store.getters.creatingTrip"></TripCreate>
  </div>
</div>
</template>

<script>
import Trip from '@/components/TripComponent'
import TripsList from '@/components/TripsList'
import TripCreate from '@/components/TripCreate'
export default {
  components:{
  TripsList,
  Trip,
  TripCreate
  }
}
</script>

<style scoped>
  #list{
    float: left;
    background-color: #8ea189;
    width: 7cm;
    height: 100%;
    align-content: left;
    text-align: left;
  }
  #content{
    float: left;
    width: calc(100% - 7cm)
  }
</style>