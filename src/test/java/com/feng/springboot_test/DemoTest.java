//package com.feng.springboot_test;
//
//public class DemoTest {
//    Map<String, String> claims = new HashMap<>();
//        claims.put("id", user.getId()+"");
//        claims.put("nickName", user.getNickName());
//        claims.put("login_method", userSocial.getLoginMethod());
//        claims.put("openId", userSocial.getUnionId());
//        claims.put("ts", Instant.now().getEpochSecond()+"");
//    String jwtToken = JwtHelper.genToken(claims);
//    // 缓存至redis
//    renewToken(jwtToken, user.getId());
//        return jwtToken;
//
//    private void renewToken(String token, int id) {
//        redisTemplate.opsForValue().set(id, token);
//        redisTemplate.expire(id, 30, TimeUnit.MINUTES);
//    }
//
//}
