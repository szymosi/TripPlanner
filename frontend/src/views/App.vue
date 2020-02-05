<template>
<div>
  <div id="list"><TripsList/></div>
  <div id="content"><BlogsList/></div>
</div>
</template>

<script>
import TripsList from '@/components/TripsList'
import BlogsList from '@/components/BlogsList'
export default {
  components:{
  TripsList,
  BlogsList
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