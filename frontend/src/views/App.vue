<template>
<div>
  <div id="list"><TripsList/></div>
  <div id="content">2</div>
</div>
</template>

<script>
import TripsList from '@/components/TripsList'
export default {
  components:{
  TripsList
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
  }
</style>