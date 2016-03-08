package pt.com.hugodias.converters.application;

import java.io.IOException;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.LineCallbackHandler;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import pt.com.hugodias.converters.qif.transactions.QIFTransaction;
import pt.com.hugodias.converters.qif.transactions.Transaction;

@Configuration
@EnableBatchProcessing
@EnableAutoConfiguration
@ComponentScan
public class BatchConfiguration {

	private String account;
	
	@Bean 
	@StepScope
	public Resource destFile(@Value("#{jobParameters[source]}") String source) {
		String dest = source.replaceAll("\\.csv", "\\.qif");
	    return new FileSystemResource(dest);
	}
	
	@Bean 
	@StepScope
	public Resource sourceFile(@Value("#{jobParameters[source]}") String source) {
	    return new FileSystemResource(source);
	}
	
	@Bean
	public ItemReader<Transaction> reader() {
		FlatFileItemReader<Transaction> reader = new FlatFileItemReader<Transaction>();
		reader.setResource(sourceFile(null));
		reader.setLineMapper(new DefaultLineMapper<Transaction>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer(";") {
					{
						setNames(
								new String[] { "transactionDate", "valueDate", "payee",  "ammount", "type", "balance", "empty" });
					}
				});
				setFieldSetMapper(new BeanWrapperFieldSetMapper<Transaction>() {
					{
						setTargetType(Transaction.class);
					}
				});
			}
		});
		reader.setLinesToSkip(13);
		reader.setComments(new String[]{"O millenniumbcp.pt"});
		reader.setSkippedLinesCallback(new LineCallbackHandler() {
			
			public void handleLine(String line) {
				Pattern p = Pattern.compile("EXTRATO DA CONTA À ORDEM Nº (\\d+)");
				Matcher m = p.matcher(line);
				if (m.find()){
					account=m.group(1);
				}
			}
		});
		return reader;
	}

    @Bean
    public ItemProcessor<Transaction, QIFTransaction> processor() {
        return new TransactionItemProcessor();
    }

	@Bean
    public ItemWriter<QIFTransaction> writer() {
		FlatFileItemWriter<QIFTransaction> writer = new FlatFileItemWriter<QIFTransaction>();
		writer.setResource(destFile(null));
		writer.setLineSeparator("\r\n");
		writer.setLineAggregator(new LineAggregator<QIFTransaction>() {
			
			public String aggregate(QIFTransaction item) {
				return item.toString();
			}
		});
		writer.setHeaderCallback(new FlatFileHeaderCallback() {
			
			public void writeHeader(Writer writer) throws IOException {
				writer.write("!Account\r\n");
				writer.write("N"+account+"\r\n");
				writer.write("TBank\r\n");
				writer.write("^");				
			}
		});
        return writer;
    }
	
    @Bean
    public Job importUserJob(JobBuilderFactory jobs, Step s1, JobExecutionListener listener) {
        return jobs.get("convertFile")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(s1)
                .end()
                .build();
    }

    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory, ItemReader<Transaction> reader,
            ItemWriter<QIFTransaction> writer, ItemProcessor<Transaction, QIFTransaction> processor) {
        return stepBuilderFactory.get("step1")
                .<Transaction, QIFTransaction> chunk(10)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

}
