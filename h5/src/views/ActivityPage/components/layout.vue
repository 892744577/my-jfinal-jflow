<template>
  <div class="_activity7_lauout">
    <slot></slot>

    <div class="bottomTabbar" v-show="!$route.meta.hideTabbar && show">
      <div class="tabbarContainer">
        <tenon-tabbar :tabs="tabbarItem" @tab-click="tabClick" />
      </div>
    </div>
  </div>
</template>

<script>
import tenonTabbar from "@/components/TenonTabbar";
export default {
  components: {
    tenonTabbar
  },
  data() {
    return {
      tabbarItem: [
        {
          name: "首页",
          id: 1
        },
        {
          name: "直播",
          id: 2
        },
        {
          name: "门店",
          id: 3
        },
        {
          name: "分享",
          id: 4
        }
      ]
    };
  },
  computed: {
    show() {
      return this.$store.state.activity.tabbarShow;
    }
  },

  methods: {
    tabClick(item) {
      switch (item.id) {
        case 1: {
          this.$router.replace({
            name: "act7index",
            query: {
              ...this.$route.query
            }
          });
          break;
        }
        case 2: {
          break;
        }
        case 3: {
          this.$router.replace({
            name: "act7shop",
            query: {
              ...this.$route.query
            }
          });
          break;
        }
        case 4: {
          this.$router.replace({
            name: "act7share",
            query: {
              ...this.$route.query
            }
          });
          break;
        }
      }
    }
  }
};
</script>

<style lang="scss">
._activity7_lauout {
  // padding-bottom: 60px;
  .bottomTabbar {
    position: fixed;
    bottom: 0;
    width: 100%;
    height: 124px;
    .tabbarContainer {
      height: 100%;
    }
  }
}
</style>