<template>
<div>
  <div id="list"><TripsList/></div>
  <div id="content"><Trip/></div>
</div>
</template>

<script>
import Trip from '@/components/TripComponent'
import TripsList from '@/components/TripsList'
export default {
  components:{
  TripsList,
  Trip
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