<template>
	<header class="gridx">
		<vs-navbar
      v-model="indexActive"
      :color="topbarColor"
      class="topnavbar"  >
      <!---
      Template logo
      --> 
      
      <!---
      Mobile toggle
      -->

      <div slot="title">
        <div class="hiddenDesktop cursor-pointer" @click.stop="activeSidebar"><vs-icon icon="menu"></vs-icon></div>
      </div>









      <vs-spacer></vs-spacer>
       

    </vs-navbar>
     
	</header>

</template>

<script>
export default {
	name : 'Navbar',
  props: {
      topbarColor: {
          type: String,
          default: "#2962ff",
      },
      title: {
            type: String,
      },
      logo: {
          type: String
      },
  },
  data:()=>({
    indexActive: 0,
    showToggle: false,
    search: ""
    
  }),

  methods: {
      //This is for sidebar trigger in mobile
      activeSidebar() {
          this.$store.commit('IS_SIDEBAR_ACTIVE', true);
      }
      
  }
}
</script>
