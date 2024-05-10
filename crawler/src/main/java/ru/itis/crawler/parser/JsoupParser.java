package ru.itis.crawler.parser;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import ru.itis.crawler.exception.InternalServerErrorCrawlerException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.util.Optional;

@ApplicationScoped
@Slf4j
public class JsoupParser {

//    @Inject
//    WebDriverParser webDriverParser;

    @Retry
    @Fallback(fallbackMethod = "fallback")
    public Optional<Document> getPage(String url) {
        try {
            log.info("Get page {}", url);
            var r = Jsoup.connect(url)
//                    .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
//                    .header("cookie", "yandexuid=8367727791609349659; _ym_uid=1609349661961055138; my=YwA=; gdpr=0; yuidss=8367727791609349659; yashr=8389691141696508973; ymex=2013281792.yrts.1697921792; amcuid=7121126071704315722; yandex_gid=43; is_gdpr=0; is_gdpr_b=CPXsfxC66wEoAg==; upa_completed=1; nec=0; utm_campaign=ymp_auto_offer_model_2_bko_dyb_search_rus; utm_medium=search; utm_source=yandex; pof=%7B%22clid%22%3A%5B%221601%22%5D%2C%22distr_type%22%3Anull%2C%22mclid%22%3Anull%2C%22opp%22%3Anull%2C%22vid%22%3Anull%2C%22erid%22%3Anull%7D; cpa-pof=%7B%22clid%22%3A%5B%221601%22%5D%2C%22distr_type%22%3Anull%2C%22mclid%22%3Anull%2C%22opp%22%3Anull%2C%22vid%22%3Anull%2C%22erid%22%3Anull%7D; i=q8MKMbfRP8Napn47BxrMuVwOKR3ZsfvzFRqo9PAmsF6fLyI+1K0JQucB1VhTm9DrGrF6EYnHOiU+PoIpIP5baJ5Alko=; L=AgxUfkNZfE9cYmZYYAcAU1J/BnV/VG9lHQkjKQk0VQM9NTMTOCgWNA==.1709232005.15634.351101.f53608247749973dff9e1d7513d3e690; yandex_login=islamgalyatdinov; isa=TuwsNvTCa1INO4noYi0BSy4P5Z3nvgYa6xgLKTgXtKLAjw4ivxTlQYkjxAJukjYXW8W+qV4jQRDvfwPvG4hal1DA7PI=; sae=0:1350E8D1-F758-4710-BF86-5122F32732FC:p:24.1.2.843:w:d:RU:20201230; _ym_isad=2; cycada=auFA17o24RPosSsMdn+jer300joMPCOQDydReqPcvg8=; Session_id=3:1709491345.5.1.1631561061456:cvME79OtE8BhNcnGoR8AKg:1e.1.2:1|1483792331.232672.2.2:232672|1485947046.4212444.2.2:4212444|1130000064770853.-1.0.2:71251685.3:1702812746|1167591539.71251601.2.2:71251601.3:1702812662|3:10283977.531661.kqs7RvYM_4Q8Lkw-FHbwOY_CNVM; sessar=1.1187.CiAh9hU0hhuYj490ijXe3239wywTAP72tkYy7BRRTOqRog.S0iWWWU1wRQAHnU5MLB_igBgEW4VGUGAdPf1LHkYg9A; sessionid2=3:1709491345.5.1.1631561061456:cvME79OtE8BhNcnGoR8AKg:1e.1.2:1|1483792331.232672.2.2:232672|1485947046.4212444.2.2:4212444|1130000064770853.-1.0.2:71251685.3:1702812746|1167591539.71251601.2.2:71251601.3:1702812662|3:10283977.531661.fakesign0000000000000000000; visits=1708266617-1708945885-1709522377; js=1; rcrr=true; spvuid_market:product_57df78_expired:1709608847582=1709522447469%2Fb333d8935777f2671b8d3736cd120600; spvuid_market:product_9e2b1f_expired:1709608860658=1709522460569%2F06953408e8e4aaab846fff36cd120600; spvuid_market:list_136c65_expired:1709608896642=1709522496571%2F7f32b81a976c40777ac92439cd120600; ys=def_bro.0#wprid.1709522525528532-5862009131891699495-balancer-l7leveler-kubr-yp-sas-244-BAL#ybzcc.ru#newsca.native_cache; gpb=gpauto.55_791820%3A49_124320%3A140%3A1%3A1709522516; receive-cookie-deprecation=1; currentRegionId=43; spvuid_market:product_81aaf7_expired:1709609943063=1709523542996%2F20eb07a3a80f35657ff48377cd120600; oq_last_shown_date=1709523723162; oq_shown_onboardings=%5B%5B%22WEB_TO_APP_default_zalogin_banner_and_popup%22%2C1709523532882%2C1714793929469%5D%2C%5B%22WEB_TO_APP_default_zalogin%22%2C1709523723162%2C1714794119510%5D%5D; parent_reqid_seq=1709523542996%2F20eb07a3a80f35657ff48377cd120600%2C1709523574552%2F164299ca78fb2a0308796579cd120600%2C1709523719241%2F355b322ac3d079e8b53f0582cd120600%2C1709524264729%2Fa237331498625d292abb88a2cd120600%2C1709524273906%2F7406ae74c465608159c114a3cd120600; global_delivery_point_skeleton={%22regionName%22:%22%D0%9A%D0%B0%D0%B7%D0%B0%D0%BD%D1%8C%22%2C%22addressLineWidth%22:45}; bh=Ek8iTm90X0EgQnJhbmQiO3Y9IjgiLCAiQ2hyb21pdW0iO3Y9IjEyMCIsICJZYUJyb3dzZXIiO3Y9IjI0LjEiLCAiWW93c2VyIjt2PSIyLjUiGgUieDg2IiIMIjI0LjEuMi44NDMiKgI/MDIJIk5leHVzIDUiOgkiV2luZG93cyJCCCIxMC4wLjAiSgQiNjQiUmYiTm90X0EgQnJhbmQiO3Y9IjguMC4wLjAiLCAiQ2hyb21pdW0iO3Y9IjEyMC4wLjYwOTkuMjgzIiwgIllhQnJvd3NlciI7dj0iMjQuMS4yLjg0MyIsICJZb3dzZXIiO3Y9IjIuNSJaAj8w; yp=1709555509.uc.ru#1709555509.duc.ru#1711317816.hdrc.1#1734348746.cld.2270452#1734348746.brd.5000004765#1711982475.csc.1#2024592005.udn.cDrQnNCw0YDQuNC90LA%3D#1947153733.multib.1#2024882527.pcs.0#1939337228.sad.1623977228:1623977228:1#1725072070.szm.1_25:1536x864:1536x748#1710447607.ygu.1#1709539230.gpauto.55_791821:49_124321:140:1:1709532030; fetch_loyalty_notifications_time_stamp=2024-03-04T06:05:49.561Z; skid=5853915711709532349; _yasc=ATaE+VMWyzLiryjnG9xDDS2qlNsvIU/c7Gf8PE2l5i7GnYo2AAk2+9iuRYmEmb16cy6FZq326RHUlSpd9/c1kN7NQbU=")
                    .header("User-Agent", "PostmanRuntime/7.36.3")
                    .header("Accept", "*/*")
                    .header("Accept-Encoding", "gzip, deflate, br")
                    .header("Connection", "keep-alive")
                    .timeout(0)
                    .method(Connection.Method.GET)
                    .execute()
                    .cookies();
//                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36")
//                    .get());

            return Optional.of(Jsoup.connect(url)
//                    .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
//                    .header("cookie", "yandexuid=8367727791609349659; _ym_uid=1609349661961055138; my=YwA=; gdpr=0; yuidss=8367727791609349659; yashr=8389691141696508973; ymex=2013281792.yrts.1697921792; amcuid=7121126071704315722; yandex_gid=43; is_gdpr=0; is_gdpr_b=CPXsfxC66wEoAg==; upa_completed=1; nec=0; utm_campaign=ymp_auto_offer_model_2_bko_dyb_search_rus; utm_medium=search; utm_source=yandex; pof=%7B%22clid%22%3A%5B%221601%22%5D%2C%22distr_type%22%3Anull%2C%22mclid%22%3Anull%2C%22opp%22%3Anull%2C%22vid%22%3Anull%2C%22erid%22%3Anull%7D; cpa-pof=%7B%22clid%22%3A%5B%221601%22%5D%2C%22distr_type%22%3Anull%2C%22mclid%22%3Anull%2C%22opp%22%3Anull%2C%22vid%22%3Anull%2C%22erid%22%3Anull%7D; i=q8MKMbfRP8Napn47BxrMuVwOKR3ZsfvzFRqo9PAmsF6fLyI+1K0JQucB1VhTm9DrGrF6EYnHOiU+PoIpIP5baJ5Alko=; L=AgxUfkNZfE9cYmZYYAcAU1J/BnV/VG9lHQkjKQk0VQM9NTMTOCgWNA==.1709232005.15634.351101.f53608247749973dff9e1d7513d3e690; yandex_login=islamgalyatdinov; isa=TuwsNvTCa1INO4noYi0BSy4P5Z3nvgYa6xgLKTgXtKLAjw4ivxTlQYkjxAJukjYXW8W+qV4jQRDvfwPvG4hal1DA7PI=; sae=0:1350E8D1-F758-4710-BF86-5122F32732FC:p:24.1.2.843:w:d:RU:20201230; _ym_isad=2; cycada=auFA17o24RPosSsMdn+jer300joMPCOQDydReqPcvg8=; Session_id=3:1709491345.5.1.1631561061456:cvME79OtE8BhNcnGoR8AKg:1e.1.2:1|1483792331.232672.2.2:232672|1485947046.4212444.2.2:4212444|1130000064770853.-1.0.2:71251685.3:1702812746|1167591539.71251601.2.2:71251601.3:1702812662|3:10283977.531661.kqs7RvYM_4Q8Lkw-FHbwOY_CNVM; sessar=1.1187.CiAh9hU0hhuYj490ijXe3239wywTAP72tkYy7BRRTOqRog.S0iWWWU1wRQAHnU5MLB_igBgEW4VGUGAdPf1LHkYg9A; sessionid2=3:1709491345.5.1.1631561061456:cvME79OtE8BhNcnGoR8AKg:1e.1.2:1|1483792331.232672.2.2:232672|1485947046.4212444.2.2:4212444|1130000064770853.-1.0.2:71251685.3:1702812746|1167591539.71251601.2.2:71251601.3:1702812662|3:10283977.531661.fakesign0000000000000000000; visits=1708266617-1708945885-1709522377; js=1; rcrr=true; spvuid_market:product_57df78_expired:1709608847582=1709522447469%2Fb333d8935777f2671b8d3736cd120600; spvuid_market:product_9e2b1f_expired:1709608860658=1709522460569%2F06953408e8e4aaab846fff36cd120600; spvuid_market:list_136c65_expired:1709608896642=1709522496571%2F7f32b81a976c40777ac92439cd120600; ys=def_bro.0#wprid.1709522525528532-5862009131891699495-balancer-l7leveler-kubr-yp-sas-244-BAL#ybzcc.ru#newsca.native_cache; gpb=gpauto.55_791820%3A49_124320%3A140%3A1%3A1709522516; receive-cookie-deprecation=1; currentRegionId=43; spvuid_market:product_81aaf7_expired:1709609943063=1709523542996%2F20eb07a3a80f35657ff48377cd120600; oq_last_shown_date=1709523723162; oq_shown_onboardings=%5B%5B%22WEB_TO_APP_default_zalogin_banner_and_popup%22%2C1709523532882%2C1714793929469%5D%2C%5B%22WEB_TO_APP_default_zalogin%22%2C1709523723162%2C1714794119510%5D%5D; parent_reqid_seq=1709523542996%2F20eb07a3a80f35657ff48377cd120600%2C1709523574552%2F164299ca78fb2a0308796579cd120600%2C1709523719241%2F355b322ac3d079e8b53f0582cd120600%2C1709524264729%2Fa237331498625d292abb88a2cd120600%2C1709524273906%2F7406ae74c465608159c114a3cd120600; global_delivery_point_skeleton={%22regionName%22:%22%D0%9A%D0%B0%D0%B7%D0%B0%D0%BD%D1%8C%22%2C%22addressLineWidth%22:45}; bh=Ek8iTm90X0EgQnJhbmQiO3Y9IjgiLCAiQ2hyb21pdW0iO3Y9IjEyMCIsICJZYUJyb3dzZXIiO3Y9IjI0LjEiLCAiWW93c2VyIjt2PSIyLjUiGgUieDg2IiIMIjI0LjEuMi44NDMiKgI/MDIJIk5leHVzIDUiOgkiV2luZG93cyJCCCIxMC4wLjAiSgQiNjQiUmYiTm90X0EgQnJhbmQiO3Y9IjguMC4wLjAiLCAiQ2hyb21pdW0iO3Y9IjEyMC4wLjYwOTkuMjgzIiwgIllhQnJvd3NlciI7dj0iMjQuMS4yLjg0MyIsICJZb3dzZXIiO3Y9IjIuNSJaAj8w; yp=1709555509.uc.ru#1709555509.duc.ru#1711317816.hdrc.1#1734348746.cld.2270452#1734348746.brd.5000004765#1711982475.csc.1#2024592005.udn.cDrQnNCw0YDQuNC90LA%3D#1947153733.multib.1#2024882527.pcs.0#1939337228.sad.1623977228:1623977228:1#1725072070.szm.1_25:1536x864:1536x748#1710447607.ygu.1#1709539230.gpauto.55_791821:49_124321:140:1:1709532030; fetch_loyalty_notifications_time_stamp=2024-03-04T06:05:49.561Z; skid=5853915711709532349; _yasc=ATaE+VMWyzLiryjnG9xDDS2qlNsvIU/c7Gf8PE2l5i7GnYo2AAk2+9iuRYmEmb16cy6FZq326RHUlSpd9/c1kN7NQbU=")
                    .header("User-Agent", "PostmanRuntime/7.36.3")
                    .header("Accept", "*/*")
                    .header("Accept-Encoding", "gzip, deflate, br")
                    .header("Connection", "keep-alive")
                    .cookies(r)
                    .timeout(0)
                    .get());
        } catch (Exception e) {
            log.error("Error when get content from url: {}", url, e);
            throw new InternalServerErrorCrawlerException(e.getMessage());
        }
    }

    public Optional<Document> fallback(String url) {
        log.info("Failed load document from url: {}", url);
        return Optional.empty();
    }
}
