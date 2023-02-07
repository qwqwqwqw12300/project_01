<template>
  <div :id="'container'+hash"></div>
</template>

<script>
import AMapLoader from '@amap/amap-jsapi-loader';

window._AMapSecurityConfig = {
  securityJsCode: "28a0f1ed7615a94ba702ffeaf920fd38", // 安全密钥
};

function createHash (hashLength) {
  // 默认长度 24
  return Array.from(Array(Number(hashLength) || 24), () => Math.floor(Math.random() * 36).toString(36)).join('');
}

export default {
  name: "GaoDeMap",
  props: {
    reLocating: {
      type: Boolean,
      default: false
    },
    value:{
      type:Array,
      default: undefined
    },
    circleRadius:{
      type:Number,
      default:100
    },
    showCircle:{
      type:Boolean,
      default:false
    }
  },
  data() {
    return {
      hash:createHash(6),
      map: undefined,
      geolocation: undefined,
      AMap:undefined,
      geocoder:undefined,
      currentLocation:undefined,
      marker:undefined,
      circle:undefined
    }
  },
  watch:{
    circleRadius: {
      deep: true,
      handler(val) {
        if(this.circle!=undefined){
          this.circle.setRadius(val);
        }
      }
    }
  },
  async mounted() {

    await this.initMap();
    if(this.value == undefined){
      this.reLocateCurrentPosition()
    }else{
      this.setCurrentPosition(this.value)
    }
  },
  methods: {
    async initMap() {
      const AMap = await AMapLoader.load({
        key: "1f82ee60ed8a674eb16ad537f748a752",             // 申请好的Web端开发者Key，首次调用 load 时必填
        version: "2.0",      // 指定要加载的 JSAPI 的版本，缺省时默认为 1.4.15
        plugins: ["AMap.Geocoder", "AMap.AutoComplete","AMap.Geolocation"],       // 需要使用的的插件列表，如比例尺'AMap.Scale'等
      })
      this.AMap = AMap
      this.map = new AMap.Map("container"+this.hash, {  //设置地图容器id
        viewMode: "3D",    //是否为3D地图模式
        zoom: 17,           //初始化地图级别
        zoomEnable: false     //地图是否可缩放
      });
      this.geolocation = new AMap.Geolocation({
        // 是否使用高精度定位，默认：true
        enableHighAccuracy: true,
        // 设置定位超时时间，默认：无穷大
        timeout: 10000,
        // 定位按钮的停靠位置的偏移量
        // offset: [10, 20],
        //  定位成功后调整地图视野范围使定位位置及精度范围视野内可见，默认：false
        zoomToAccuracy: true,
        //  定位按钮的排放位置,  RB表示右下
        position: 'RB'
      })
      this.geocoder = new AMap.Geocoder()
    },
    reLocateCurrentPosition() {
      this.$emit('update:reLocating',true);
      this.geolocation.getCurrentPosition((status, result) => {
        this.$emit('update:reLocating',false);
        if (status == 'complete') {
          this.setCurrentPosition([result.position.getLng(),result.position.getLat()])
        }
      });
    },
    setCurrentPosition(location){
      this.map.setCenter(location);

      if(this.marker!=undefined){
        this.marker.setMap(null)
        this.marker = undefined
      }
      this.marker = new this.AMap.Marker({
        icon: "http://a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
        position:location,
        offset: new AMap.Pixel(-26, -57)
      });
      this.marker.setMap(this.map);

      if(this.circle!=undefined){
        this.map.remove(this.circle)
        this.circle = undefined
      }

      if(this.showCircle){
        this.circle = new this.AMap.Circle({
          center: location,
          radius: this.circleRadius, //半径
          borderWeight: 3,
          strokeColor: "#FF33FF",
          strokeWeight: 6,
          strokeOpacity: 0.2,
          fillOpacity: 0.4,
          strokeStyle: 'dashed',
          strokeDasharray: [10, 10],
          // 线样式还支持 'dashed'
          fillColor: '#1791fc',
          zIndex: 50,
        })
        this.map.add(this.circle)
      }


      this.geocoder.getAddress(location, (status, result) => {
        if (status === 'complete' && result.regeocode) {
          this.$emit('update:address', result.regeocode.formattedAddress)
        } else {
          log.error('根据经纬度查询地址失败')
        }
      })
    }
  },
}
</script>

<style scoped>
.container {
  padding: 0px;
  margin: 0px;
  width: 100%;
  height: 800px;
}
</style>
