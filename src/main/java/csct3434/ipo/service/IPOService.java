package csct3434.ipo.service;

import csct3434.ipo.web.domain.IPO;
import csct3434.ipo.web.domain.IPORepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class IPOService {

    private final IPORepository ipoRepository;

    public IPO findByStockCode(int stockCode) {
        return ipoRepository.findByStockCode(stockCode);
    }

    public void save(IPO ipo) {
        if(ipoRepository.findByStockCode(ipo.getStockCode()) == null) {
            ipoRepository.save(ipo);
        }
    }

}
